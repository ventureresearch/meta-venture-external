DESCRIPTION = "sslh - ssl/ssh multiplexer"
HOMEPAGE = "http://www.rutschle.net/tech/sslh.shtml"
SECTION = "console/network"
LICENSE = "GPLv2"
PRIORITY = "optional"

PR = "r0"

SRC_URI = "http://www.rutschle.net/tech/sslh-${PV}.tar.gz \
    file://init-sslh \
    "

DEPENDS = "libconfig"

SRC_URI[md5sum] = "36e64bd6c7d0a299d5acd1109988099b"
SRC_URI[sha256sum] = "028922071cf6bb5816de3aa70f4f451ee2ce2f4a704bc18d2984334976cce8e7"

LIC_FILES_CHKSUM = "file://sslh-main.c;startline=6;endline=19;md5=11868e3e8f195107a4b30fbfc62db922"

inherit update-rc.d

INITSCRIPT_NAME = "sslh"
INITSCRIPT_PARAMS = "defaults 20"

do_install () {
    install -d ${D}${sbindir}
# use the sslh-fork version for an embedded device
    install -m 0700 ${S}/sslh-fork ${D}${sbindir}/sslh

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init-sslh ${D}${sysconfdir}/init.d/sslh
}    

FILES_${PN} += "${sysconfdir}/init.d/sslh ${sbindir}/sslh"
