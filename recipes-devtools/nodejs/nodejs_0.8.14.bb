DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95a589a3257ab7dfe37d8a8379e3c72d"

DEPENDS = "openssl"

SRC_URI = "http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz \
           file://0001-Fix-hardfloat-detection.patch \
           file://0002-Enable-NEON.patch \
           file://0003-Resolve-bad-rpath-issue-when-linking.patch \
          "
SRC_URI[md5sum] = "284fd2c7578064c339d9cf6a3a475ac7"
SRC_URI[sha256sum] = "e5ce2aadb4df3ea4ca7a021106ffe09d286474476454038e9ed0135eac18e6d0"

S = "${WORKDIR}/node-v${PV}"

# v8 errors out if you have set CCACHE
CCACHE = ""

do_configure () {
  export LD="${CXX}"
  #./configure --without-snapshot --dest-cpu=arm --dest-os=linux --prefix=${prefix}
  ./configure --prefix=${prefix} --without-snapshot
}

do_install () {
  DESTDIR=${D} oe_runmake install
}

RDEPENDS_${PN} = "curl python-shell python-datetime python-subprocess python-crypt python-textutils python-netclient "
RDEPENDS_${PN}_virtclass-native = "curl-native python-native"

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules"
BBCLASSEXTEND = "native"
