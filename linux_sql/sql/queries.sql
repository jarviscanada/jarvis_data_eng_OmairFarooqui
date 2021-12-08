--Group hosts by hardware info
SELECT  cpu_number, id, total_mem FROM host_usage GROUP BY cpu_number, id, total_mem ORDER BY total_mem DESC;

CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

SELECT x.host_id, x.hostname, round5(x.timestamp), AVG(((y.total_mem - x.memory_free)*100)/y.total_mem) AS avg_used_mem_percentage
FROM host_usage AS y
LEFT JOIN host_info as x ON y.id=x.host_id
GROUP BY x.host_id, y.hostname, round5(x.timestamp);


SELECT host_id, round5(timestamp)
COUNT(*) AS data_points
FROM host_info

IF data_points < 3 THEN
    GROUP BY host_id, timestamp;
END IF;