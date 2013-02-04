DESCRIPTION = "node-sqlite3 - Asynchronous, non-blocking SQLite3 bindings for Node.js"
HOMEPAGE = "https://github.com/developmentseed/node-sqlite3"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=df600e0b6e9f49ab679b423a4588f7a5"

PR="r5"

DEPENDS = "nodejs sqlite3"


SRC_URI = "git://github.com/developmentseed/node-sqlite3.git;protocol=https \
          "

SRCREV="1173659e01ebf949e711a3a20d8bdca0cc6bcc75"

S = "${WORKDIR}/git"

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

RDEPENDS_${PN} = "sqlite3"
