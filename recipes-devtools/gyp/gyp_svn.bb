DESCRIPTION = "Generate Your Projects, a build system"
SECTION = "devel/python"
HOMEPAGE = "https://code.google.com/p/gyp/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ab828cb8ce4c62ee82945a11247b6bbd"
PR = "r1"

DEPENDS = "python-native"

SRC_URI = "svn://gyp.googlecode.com/svn/;protocol=http;module=trunk"
SRCREV="1569"

S = "${WORKDIR}/trunk"

inherit distutils native pythonnative

