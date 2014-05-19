#!/bin/bash

cd $1
targetpath=$(pwd)
cd -
pwd=$(pwd)
name=$0
dir=$(dirname ${name})
cd ${dir}

_jar=`ls lib | grep "..*\.jar$"`

classpath='. '${_jar}
classpath=`echo ${classpath} | sed -e 's/ /:lib\//g'`

java -cp ${classpath} com.chenjw.issuemonitor.StartMain $targetpath

cd -
