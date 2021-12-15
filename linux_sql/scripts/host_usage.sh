psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ $# -ne 5 ]; then
    echo 'Please provide: psql_host psql_port db_name psql_user psql_password'
    exit 1
fi

lscpu_out=`lscpu`
hostname=$(hostname -f)

cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "Model:" | awk '{print $2}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "L2 cache:" | awk '{print $3}' | xargs)
l2_cache=${l2_cache//[^[:digit:].-]/}
total_mem=$(cat /proc/meminfo  | egrep "MemTotal:" | awk '{print $2}' | xargs)

timestamp=$(vmstat -t | awk '{print $18" "$19}' | xargs)

#host_id="(SELECT id FROM host_usage WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage(hostname, id, timestamp, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem) VALUES ('$hostname',(SELECT host_id FROM host_info WHERE hostname='$hostname'), '$timestamp', '$cpu_number','$cpu_architecture','$cpu_model','$cpu_mhz','$l2_cache', '$total_mem')"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?