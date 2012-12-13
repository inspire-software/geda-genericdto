#!/bin/sh 

FROMREPO=https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
TOREPO=https://geda-genericdto.googlecode.com/svn
svnsync --non-interactive sync $TOREPO
