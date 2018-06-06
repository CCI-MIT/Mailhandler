#!/bin/bash

eval "$(ssh-agent -s)"
chmod 600 /tmp/deploy_rsa
ssh-add /tmp/deploy_rsa

WORKER_IP="$(dig +short myip.opendns.com @resolver1.opendns.com)"

DEPLOY_SERVER=binaries@cognosis2.mit.edu
DEPLOY_FOLDER=mailhandler
echo "Copying binaries from ${WORKER_IP} to ${DEPLOY_SERVER}:${DEPLOY_FOLDER} ..."
scp target/mailhandler-*.jar ${DEPLOY_SERVER}:${DEPLOY_FOLDER}/

# Workaround for hanging builds
# taken from https://github.com/travis-ci/travis-ci/issues/8082#issuecomment-315147561
ssh-agent -k
