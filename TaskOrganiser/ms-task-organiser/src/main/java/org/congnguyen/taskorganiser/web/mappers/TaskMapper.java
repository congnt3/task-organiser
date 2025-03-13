package org.congnguyen.taskorganiser.web.mappers;

import org.apache.commons.lang3.StringUtils;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.persistence.exceptions.RecordNotFoundException;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.congnguyen.taskorganiser.web.models.graph.Edge;
import org.congnguyen.taskorganiser.web.models.graph.Graph;
import org.congnguyen.taskorganiser.web.models.graph.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    @Autowired
    TaskRepository taskRepository;

    @Mapping(target = "externalLinks", ignore = true)
    @Mapping(target = "parent", source = "parentCode", qualifiedByName = "codeToTask")
    @Mapping(target = "dependsOn", source = "dependsOnTasks", qualifiedByName = "codesToTasks")
    public abstract Task createTaskRequestToTask(CreateTaskRequest request);

    /**
     * Return the mapped TaskModel object with only first level dependencies
     *
     * @param task
     * @return
     */
    @Mapping(target = "parentCode", source = "parent.code")
    @Mapping(target = "dependsOn", source = "dependsOn", qualifiedByName = "mapDependsOn")
    public abstract TaskModel taskToTaskModel(Task task);

    /**
     * Return the mapped TaskModel object without dependencies
     *
     * @param task
     * @return
     */
    @Named("TaskToTaskModelNoDeps")
    @Mapping(target = "parentCode", source = "parent.code")
    @Mapping(target = "dependsOn", ignore = true)
    public abstract TaskModel taskToTaskModelNoDeps(Task task);

    public Graph<TaskModel> tasksToTaskDepsGraph(List<Task> tasks) {
        Graph<TaskModel> graph = new Graph<>();
        tasksToTaskDepsGraph(tasks, graph);

        return graph;
    }

    public List<Node<TaskModel>> tasksToTaskDepsGraph(List<Task> tasks, Graph<TaskModel> wipGraph) {
        var result = new ArrayList<Node<TaskModel>>();
        //Map list of tasks to Graph object with list of Node<TaskModel> and edges
        Graph<TaskModel> graph = wipGraph == null ? new Graph<>() : wipGraph;

        tasks.forEach(t -> {
            var existingNode = graph.getNodes().stream().filter(node -> t.getCode().equalsIgnoreCase(node.getData().getCode())).findFirst();
            if (existingNode.isEmpty()) {
                var nodeT = this.taskToTaskModelGraphNode(t);

                var dependencies = t.getDependsOn();
                List<Node<TaskModel>> convertedNodes = new ArrayList<>();
                if (dependencies != null) {
                    //Add all dependencies
                    convertedNodes.addAll(this.tasksToTaskDepsGraph(dependencies, graph));
                    if (nodeT.getLabel() == "US12345715"){
                        System.out.println("US12345715");
                    }
                    nodeT.setLinked(convertedNodes);
                    //Add all edges
                    dependencies.forEach(d -> {
                        var edge = new Edge();
                        edge.setId(String.format("%s-%s", t.getCode(), d.getCode()));
                        edge.setSource(t.getCode());
                        edge.setTarget(d.getCode());
                        graph.getEdges().add(edge);
                    });
                }

                graph.getNodes().add(nodeT);
                result.add(nodeT);
            }
            else { // if the node already exist in the graph
                result.add(existingNode.get());
            }
        });

        return result;
    }

    @Mapping(target = "data", source = ".", qualifiedByName = "TaskToTaskModelNoDeps")
    @Mapping(target = "id", source = "code")
    @Mapping(target = "label", source = "code")
    public abstract Node<TaskModel> taskToTaskModelGraphNode(Task task);

    @Named("codeToTask")
    protected Task stringToTask(String code) throws RecordNotFoundException {
        if (!StringUtils.isNotEmpty(code)) {
            return null;
        }

        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not found", code)));
    }

    @Named("codesToTasks")
    protected List<Task> codesToTasks(Collection<String> codes) throws RecordNotFoundException {
        if (codes == null) {
            return null;
        }
        var tasks = new ArrayList<Task>();
        for (String code : codes) {
            var task = taskRepository.findByCode(code);
            if (!task.isPresent()) {
                throw new RecordNotFoundException(String.format("Task with code %s not found", code));
            }

            tasks.add(task.get());
        }

        return tasks;
    }

    @Mapping(target = "dependsOn", ignore = true)
    public abstract Task copyTaskEntityWithoutDependencies(Task input);

    @Named("mapDependsOn")
    protected List<TaskModel> mapDependsOn(List<Task> input) {
        if (input == null) {
            return null;
        }

        var result = input.stream()
                .map(this::copyTaskEntityWithoutDependencies)
                .map(this::taskToTaskModel).toList();
        return result;
    }
}
