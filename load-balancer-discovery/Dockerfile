FROM haproxy:2.7
USER root
RUN apt-get update && apt-get install -y socat && rm -rf /var/lib/apt/lists/*
EXPOSE 80
EXPOSE 9999
EXPOSE 8404
COPY haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg
# FROM alpine

# RUN apk --no-cache add socat

# ENTRYPOINT ["socat"]