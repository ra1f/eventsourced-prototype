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

>curl -H "Content-Type: application/json" -d '[{"command":"Digest", "animalId":"Lions", "timestamp":"2015-03-31T14:17:01.123-0700"}]' http://localhost:8080/commands

>curl -H "Content-Type: application/json" -d '[{"command":"Digest", "animalId":"Lions", "timestamp":"2015-03-31T14:17:01.123-0700"},
{"command":"Sadden", "animalId":"Monkeys", "timestamp":1400000000000}]' http://localhost:8080/commands

