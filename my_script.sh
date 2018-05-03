#!/bin/bash

rm /usr/local/share/ca-certificates/OWASP_ZAP.crt
rm /etc/ssl/certs/OWASP_ZAP.pem
curl http://localhost:$1/OTHER/core/other/rootcert/ > /usr/local/share/ca-certificates/OWASP_ZAP.crt
update-ca-certificates 
