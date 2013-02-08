DESCRIPTION = "node-sqlite3 - Asynchronous, non-blocking SQLite3 bindings for Node.js"
HOMEPAGE = "https://github.com/developmentseed/node-sqlite3"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://package.json;md5=a9ea81e283fcd9d9b3cd7c41a025334e;startline=26;endline=26"

PR="r5"

DEPENDS = "nodejs nodejs-native python-native sqlite3"
RDEPENDS = "nodejs"

SRC_URI = "git://github.com/ventureresearch/node-sqlite-fts.git;protocol=https \
          "

inherit pythonnative

SRCREV="f376a9326576cde97911bc61558da7b3368e6b98"

S = "${WORKDIR}/git"

# v8 errors out if you have set CCACHE
CCACHE = ""

do_configure () {
  export LD="${CXX}"
  export CFLAGS="${CFLAGS} -fPIC"
  export CXXFLAGS="${CXXFLAGS} -fPIC"
  node-gyp configure -v
}

do_compile() {
  export LD="${CXX}"
  export CFLAGS="${CFLAGS} -fPIC"
  export CXXFLAGS="${CXXFLAGS} -fPIC"
  node-gyp build -v
}

do_install () {
  install -d ${D}/${libdir}/node_modules/npm/node_modules/sqlite3-fts
  install -m 0644 ${S}/build/Release/sqlite3_bindings.node ${D}/${libdir}/node_modules/npm/node_modules/sqlite3-fts/sqlite3-fts.node
}

FILES_${PN} = "${libdir}/node_modules/npm/node_modules/sqlite3-fts/sqlite3-fts.node"

RDEPENDS_${PN} = "sqlite3"
