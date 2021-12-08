# Linux Cluster Monitoring Agent
This project is under development. Since this project follows the GitFlow, the final work will be merged to the master branch after Team Code Team.

# Introduction
The project is designed to record the hardware specifications and monitor resources usages (CPU/memory etc) in real-time of each node within a Linux cluster of 10 nodes. These nodes will be running CentOS 7 and connected via switch, allowing them to communicate through internal IPv4 addresses. The data collected will be stored in a PostgreSQL database and used to generate reports for resource planning. The technologies used in this project include: Bash (scripts), IntelliJ IDE, Docker, Git, and PostgreSQL.

# Quick Start
Start a psql instance using psql_docker.sh
```
bash psql_docker.sh start
```
Create tables using ddl.sql
```
Psql -h localhost -U omair -d host_agent -f ddl.sql
```
Insert hardware specs data into the DB using host_usage.sh

```
INSERT INTO host_usage(hostname, id, timestamp, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem) VALUES ('$hostname',(SELECT host_id FROM host_info WHERE hostname='$hostname'), '$timestamp', '$cpu_number','$cpu_architecture','$cpu_model','$cpu_mhz','$l2_cache', '$total_mem')
```
Insert hardware usage data into the DB using host_info.sh
```
INSERT INTO host_info(hostname, timestamp, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$hostname', '$timestamp', '$memory_free','$cpu_idle','$cpu_kernel','$disk_io','$disk_available')
```
Crontab setup
```
bash> crontab -e
* * * * * bash /home/centos/dev/jarvis_data_eng_omair/linux_sql/scripts/host_usage.sh localhost 5432 host_agent omair omair > /tmp/host_usage.log
```

# Implementation
Discuss how you implement the project.
## Architecture 
Draw a cluster diagram with three Linux hosts, a DB, and agents (use draw.io website). Image must be saved to the `assets` directory.

## Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh
  - This shell script was used to set up the psql instance using docker.
  ```
  bash psql_docker create|start|stop [username] [password]
  ```
- host_info.sh
  - This shell script is used to collect hardware specification data and insert it into the psql database. 
  ```
  bash host_info.sh psql_host psql_port db_name psql_user psql_password
  ```
- host_usage.sh
  - This shell script is used to collect the server usage data and insert it into the psql database.
  ```
  bash host_usage.sh psql_host psql_port db_name psql_user psql_password
  ```
- crontab
  - This script is used to execute host_usage.sh every minute continuously. 
  ```
  crontab -e
  * * * * * bash /home/centos/dev/jarvis_data_eng_omair/linux_sql/scripts/host_usage.sh localhost 5432 host_agent omair omair > /tmp/host_usage.log
  ```
- queries.sql 
  - This script is used to extract data to manage the cluster better and plan for future resources.

## Database Modeling
`host_info`

| timestamp | host_id (PRIMARY KEY)| hostname | memory_free | cpu_idle | cpu_kernel | disk_io | disk_available |
| ----------- | ------- | -------- | --- | ---- | ----- | -------| ----- |

`host_usage`

| id (FOREIGN KEY to host_id)| hostname | cpu_number | cpu_architecture | cpu_model | cpu_mhz | L2_cache | total_mem | timestamp |
| ----------- | ------- | -------- | --- | ---- | ----- | -------| ----- | ----|
# Test
Bash scripts and SQL Queries were manually tested.

psql_docker.sh: the following commands were used to check if the container is created and running. The results received showed no errors. 
```
docker container ls -a -f name=jrvs-psql
docker ps -f name=jrvs-psql
```
ddl.sql: with the use of DBeaver, the table format and entities were simply checked. 

host_info.sh and host_usage.sh: were both tested using DBeaver. After running the scripts, I simply checked to see if the correct data was being entered and manually ran the CLI commands to cross-check all hardware specifications and usage data. 

Crontab: the following commands were run to check if data was being inserted every minute.
```
psql -h localhost -p 5432 -d host_agent -U omair 
SELECT * FROM host_usage;
```

SQL Queries: DBeaver was also used to verify the SQL script entries and manually test if the correct output was displayed.

# Deployment
This application was deployed with the use of Git/Github, Docker, a Google Cloud Platform Virtual Machine Instance using CentOS 7 Linux, Bash Scripting, PostgreSQL, Crontab, and IntelliJ IDE. 

# Improvements
- Fix SQL queries round5 function to take timestamp without UTC timezone.
- Improve linux usage data commands used to reduce code being written.
- When running the bash scripts that use the psql instance, try to get it to work with postgres user, instead of my own made user.
