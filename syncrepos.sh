#!/bin/sh 

FROMREPO=https://svn.code.sf.net/p/geda-genericdto/code/
TOREPO=https://geda-genericdto.googlecode.com/svn
svnsync --non-interactive sync $TOREPO
# svnsync sync $TOREPO
