#!/bin/sh
set -eu

cat >/tmp/.my.cnf <<EOF
[client]
user=${EXPORTER_USER}
password=${EXPORTER_PASSWORD}
host=${EXPORTER_HOST:-mysql}
port=${EXPORTER_PORT:-3306}
EOF

exec /bin/mysqld_exporter --config.my-cnf=/tmp/.my.cnf