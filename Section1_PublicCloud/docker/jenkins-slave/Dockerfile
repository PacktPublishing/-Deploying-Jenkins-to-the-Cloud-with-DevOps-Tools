# jenkins container with addons
#
# VERSION               0.2

FROM jenkinsci/ssh-slave

# make sure the package repository is up to date
RUN apt-get update -q

# install sshpass
RUN apt-get -y install sshpass

# Install Chrome
RUN cd /tmp  &&\
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb &&\
    dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install &&\
    apt-get -y install xvfb