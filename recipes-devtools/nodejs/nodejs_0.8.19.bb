DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95a589a3257ab7dfe37d8a8379e3c72d"

PR="r5"

DEPENDS = "openssl"

SRC_URI = "http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz \
           file://0001-Fix-hardfloat-detection.patch \
           file://0002-Enable-NEON.patch \
          "

SRC_URI[md5sum] = "db70f9ce88ce460c43dc57bec3a0fb6a"
SRC_URI[sha256sum] = "703207d7b394bd3d4035dc3c94b417ee441fd3ea66aa90cd3d7c9bb28e5f9df4"

S = "${WORKDIR}/node-v${PV}"

# v8 errors out if you have set CCACHE
CCACHE = ""

do_configure () {
  export LD="${CXX}"
  #./configure --without-snapshot --dest-cpu=arm --dest-os=linux --prefix=${prefix}
  ./configure --prefix=${prefix} --without-snapshot
}

do_install () {
  export DESTDIR="${D}"
  oe_runmake install
}

RDEPENDS_${PN} = "curl python-shell python-datetime python-subprocess python-crypt python-textutils python-netclient "
RDEPENDS_${PN}_virtclass-native = "curl-native python-native"

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules"
BBCLASSEXTEND = "native"
