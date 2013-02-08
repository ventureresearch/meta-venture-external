DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95a589a3257ab7dfe37d8a8379e3c72d"

PR="r5"

DEPENDS = "openssl"

SRC_URI = "http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz \
           file://0001-Fix-hardfloat-detection.patch \
           file://0002-Enable-NEON.patch \
           file://node-gyp \
          "

SRC_URI[md5sum] = "db70f9ce88ce460c43dc57bec3a0fb6a"
SRC_URI[sha256sum] = "703207d7b394bd3d4035dc3c94b417ee441fd3ea66aa90cd3d7c9bb28e5f9df4"

S = "${WORKDIR}/node-v${PV}"

inherit native

# v8 errors out if you have set CCACHE
CCACHE = ""

do_configure () {
  export LD="${CXX}"
  ./configure --prefix=${prefix}
}

do_install() {
  export DESTDIR="${D}"
  oe_runmake install
  install -m 0755 ${WORKDIR}/node-gyp ${D}${bindir}/node-gyp
}

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules ${bindir}/npm ${bindir}/node-gyp"
