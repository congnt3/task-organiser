package org.congnguyen.taskorganiser.web.services;

import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.congnguyen.taskorganiser.web.models.graph.Graph;
import org.congnguyen.taskorganiser.web.models.graph.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
public class TaskGraphOrderService implements GraphOrderService<TaskModel> {
    private static final int MAX_CHILD_COUNT = 100;

    @Override
    public Graph markDepLevel(Graph<TaskModel> obj) {
        if (obj == null || obj.getNodes() == null
                || obj.getNodes().isEmpty()) {
            return obj;
        }

        obj.getNodes().forEach(this::markDepLevel);

        return obj;
    }

    private void markDepLevel(Node<TaskModel> node) {
        if (node.getData().getCode() == "US12345715"){
            System.out.println("debug");
        }
        if (node == null || node.getDepLevel() >= 0) {
            return;
        }

        // No dependency then lvl0
        if (node.getLinked() == null ||
                node.getLinked().isEmpty()) {
            node.setDepLevel(0);
            return;
        }

        node.getLinked().forEach(this::markDepLevel);
        node.setDepLevel(node.getLinked().stream()
                .map(Node::getDepLevel)
                .max(Comparator.comparingInt(n -> n))
                .orElse(0) + 1);
    }

    @Override
    public Graph markOrder(Graph<TaskModel> obj) {
        if (obj == null || obj.getNodes() == null
                || obj.getNodes().isEmpty()) {
            return obj;
        }

        // find max depLevel
        var maxDepLvl = obj.getNodes().stream()
                .map(Node::getDepLevel)
                .max(Comparator.comparingInt(n -> n))
                .orElse(0);
        // create ArrayList with maxDepLvl + 1 elements with all elements initiated
        var displayMap = new ArrayList<ArrayList<Node<TaskModel>>>(maxDepLvl + 1);
        for (int i = 0; i <= maxDepLvl; i++) {
            displayMap.add(new ArrayList<>());
        }

//        obj.getNodes().forEach(n -> this.markOrder(n, firstLvlCounter));
        obj.getNodes().forEach(n -> {
            displayMap.get(n.getDepLevel()).add(n);
        });

        for (int i = 0; i <= maxDepLvl; i++) {
            displayMap.get(i).forEach(n -> this.markOrder(n, displayMap));
            displayMap.get(i).sort(Comparator.comparingInt(Node::getLevelIndex));
        }


        return obj;
    }

    private void markOrder(Node<TaskModel> node,
                           ArrayList<ArrayList<Node<TaskModel>>> displayMap) {
        if (node == null || node.getLevelIndex() >= 0) {
            return;
        }

        if (node.getDepLevel() == 0) {
            node.setLevelIndex(displayMap.get(node.getDepLevel()).indexOf(node) * MAX_CHILD_COUNT);
            setPosition(node, displayMap);
            return;
        }
        Integer depsMaxLevel = node.getLinked().stream()
                .map(Node::getDepLevel)
                .max(Comparator.comparingInt(n -> n))
                .orElse(0);
        var relatedSuperNodeList = node.getLinked().stream().filter(n -> depsMaxLevel.equals(n.getDepLevel())).toList();
        var relatedSuperNode = relatedSuperNodeList.stream().min(Comparator.comparingInt(Node::getLevelIndex)).orElseThrow();
        relatedSuperNode.setDependantCount(relatedSuperNode.getDependantCount() + 1);
        node.setLevelIndex(relatedSuperNode.getDependantCount() +
                MAX_CHILD_COUNT * displayMap.get(relatedSuperNode.getDepLevel()).indexOf(relatedSuperNode));
        setPosition(node, displayMap);

    }

    private void setPosition(Node<TaskModel> node,
                             ArrayList<ArrayList<Node<TaskModel>>> displayMap) {

        if (node == null) {
            return;
        }

        var x = displayMap.get(node.getDepLevel()).indexOf(node) * 200;
        var y = (node.getDepLevel() + 1) * -300;
        node.getPosition().setX(x);
        node.getPosition().setY(y);
    }
}
