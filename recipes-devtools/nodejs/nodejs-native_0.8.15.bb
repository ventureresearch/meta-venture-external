DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95a589a3257ab7dfe37d8a8379e3c72d"

PR="r5"

DEPENDS = "openssl"

SRC_URI = "http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz \
           file://0001-Fix-hardfloat-detection.patch \
           file://0002-Enable-NEON.patch \
           file://0003-Resolve-bad-rpath-issue-when-linking.patch \
          "

SRC_URI[md5sum] = "6cb31180b07475db103e694f65e8bb9b"
SRC_URI[sha256sum] = "1758639c6df3e081fe26585472d0f1961c5703b44ba6c57ecdf66a4c015792b1"

S = "${WORKDIR}/node-v${PV}"

inherit native

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

do_gyp() {
  npm install -g node-gyp
}
addtask gyp after do_install before do_package

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules ${bindir}/node-gyp"
