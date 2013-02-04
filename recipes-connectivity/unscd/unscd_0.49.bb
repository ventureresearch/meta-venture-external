DESCRIPTION = "unscd, a complete nscd replacement."
HOMEPAGE = "http://busybox.net/~vda/unscd/"
SECTION = "console/network"
LICENSE = "GPLv2"
PRIORITY = "optional"

PR = "r0"

SRC_URI = "http://busybox.net/~vda/unscd/nscd-${PV}.c \
    file://init-nscd \
    file://nscd.conf \
    "

SRC_URI[md5sum] = "ee9cdaac340635e0c14551febbc0fd22"
SRC_URI[sha256sum] = "a7bef9d799a15239a00fd58dd627c5ff2cb245fc4a798b3453d08ff6a730c134"

LIC_FILES_CHKSUM = "file://../nscd-${PV}.c;startline=5;endline=16;md5=9d2d9eda8be2b534ad04240a8c79fbb8"

inherit update-rc.d

INITSCRIPT_NAME = "nscd"
INITSCRIPT_PARAMS = "defaults 50"

do_configure() {
    cp ${WORKDIR}/nscd-${PV}.c ${S}/
}

do_compile() {
    $CC nscd-${PV}.c -o nscd
}

# unscd calls itself nscd any time it is used, so stick with that convention

do_install () {
    install -d ${D}${sbindir}
    install -m 0700 ${S}/nscd ${D}${sbindir}/nscd

    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/nscd.conf ${D}${sysconfdir}/nscd.conf

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init-nscd ${D}${sysconfdir}/init.d/nscd
}    

FILES_${PN} += "${sysconfdir}/nscd.conf ${sysconfdir}/init.d/nscd ${sbindir}/nscd"
