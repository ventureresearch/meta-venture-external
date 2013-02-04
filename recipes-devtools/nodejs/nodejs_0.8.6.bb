DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT"

DEPENDS = "openssl"

PR = "r3"

SRC_URI = " \
  http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz \
"

LIC_FILES_CHKSUM = "file://LICENSE;md5=95a589a3257ab7dfe37d8a8379e3c72d"

SRC_URI[md5sum] = "dd722d92f4f3a05352b43de99b951a50"
SRC_URI[sha256sum] = "dbd42800e69644beff5c2cf11a9d4cf6dfbd644a9a36ffdd5e8c6b8db9240854"


S = "${WORKDIR}/node-v${PV}"

# v8 errors out if you have set CCACHE
CCACHE = ""

do_configure () {
  ./configure --prefix=${prefix} --without-snapshot
}

do_compile () {
  make
}

do_install () {
  DESTDIR=${D} oe_runmake install
}

RDEPENDS_${PN} = "curl python-shell python-datetime python-subprocess python-crypt python-textutils python-netclient "
RDEPENDS_${PN}_virtclass-native = "curl-native python-native"

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules"

BBCLASSEXTEND = "native"
