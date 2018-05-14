#!/bin/bash

sudo rm /usr/local/share/ca-certificates/OWASP_ZAP.crt
sudo rm /etc/ssl/certs/OWASP_ZAP.pem
sudo curl http://localhost:$1/OTHER/core/other/rootcert/ > /usr/local/share/ca-certificates/OWASP_ZAP.crt
sudo update-ca-certificates

#Initialisation of OWASP CA
certname="OWASP CA"
certfile="/etc/ssl/certs/OWASP_ZAP.pem"

#Creation of webdriver profile
firefox -CreateProfile WebDriver

#Copy/Paste of cert.db and key.db
for file in $(find ~/.mozilla/firefox/*.default -name "*.db")
do
    cp ${file} ~/.mozilla/firefox/*.WebDriver
done

#AddOWASP certificate to the firefox cert.db in each of the profile
for certDB in $(find ~/.mozilla/firefox -name "cert*.db")
do
    certdir=$(dirname ${certDB});
    certutil -A -n "${certname}" -t "TCu,Cu,Tu" -i ${certfile} -d sql:${certdir}
done
