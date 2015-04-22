# eventsourced-prototype 

## Starting Postgres on Docker
### Preconditions
If you decide to use Postgres on Docker change directory:
> cd postgres

If you are using Windows or MacOS Vagrant and VirtualBox has to be installed at first. On Linux Docker environment has to be
installed.

When using Vagrant and sitting behind a firewall the vagrant-proxyconf is also needed. The Vagrant configuration is looking for 
the host environment variable HTTP_PROXY or http_proxy and will use it for both http and https. (If this does not match your
host configuration as having a separate variable for HTTPS_PROXY you can reconfigure the Vagrantfile accordingly) 
 
>vagrant plugin install vagrant-proxyconf

### Start the VM
Start the VM like this:
>vagrant up

Make sure there are no errors logged into the console.

Login to the VM:
>vagrant ssh

### Start the database
Change directory to the mount point:
>cd /vagrant


## REST requests

Zookeeper: Buy a new animal
>$ curl -H "Content-Type: application/json" -X PUT -d '{"animalId":"Lion"}' http://localhost:8080/buy

>{"sequenceId":0}

Mother Nature: Digest
>$ curl -i -H "Content-Type: application/json" -X PUT -d '{"animalId":"Lion", "sequenceId": 1}' http://localhost:8080/digest

>{"sequenceId":1}

Ask for state: Animal is hungry now
$ curl -H "Content-Type: application/json" -X GET http://localhost:8080/animals/Lion

>{"animalId":"Lion","sequenceId":1,"lastOccurence":1429709244114,"feelingOfSatiety":"hungry","mindstate":"happy","hygiene":"tidy","version":1}

Zookeeper: Feed the animal
>$ curl -i -H "Content-Type: application/json" -X PUT -d '{"animalId":"Lion", "sequenceId": 2}' http://localhost:8080/feed

>{"sequenceId":2}

Ask for state: Animal is full again
$ curl -H "Content-Type: application/json" -X GET http://localhost:8080/animals/Lion

>{"animalId":"Lion","sequenceId":1,"lastOccurence":1429709251222,"feelingOfSatiety":"full","mindstate":"happy","hygiene":"tidy","version":2}





