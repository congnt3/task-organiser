To start neo4j docker:
docker run \
--restart always \
--publish=7474:7474 --publish=7687:7687 \
--env NEO4J_AUTH=neo4j/pass@word1 \
--volume=/Users/kevinnguyen/data:/data \
--user="$(id -u):$(id -g)"     neo4j:latest