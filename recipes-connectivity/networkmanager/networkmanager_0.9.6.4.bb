DESCRIPTION = "NetworkManager"
SECTION = "net/misc"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b"

PR = "r6"

DEPENDS = "libnl dbus dbus-glib udev wireless-tools polkit gnutls util-linux ppp"
inherit gnome gettext

SRC_URI = "${GNOME_MIRROR}/NetworkManager/${@gnome_verdir("${PV}")}/NetworkManager-${PV}.tar.xz \
    file://0001-don-t-try-to-run-sbin-dhclient-to-get-the-version-nu.patch;patch=1 \
    file://print-in-replace-default.patch;patch=1 \
    file://busybox-init-functions.patch;patch=1 \
    file://gtk-doc.make \
    file://keyfile-T-mobile \
    file://keyfile-Verizon \
    file://NetworkManager.conf \
    file://org.freedesktop.NetworkManager.conf \
"
SRC_URI[md5sum] = "54ca5200edeb5155086ced43d00b0cad"
SRC_URI[sha256sum] = "511b411e055d187bc8f26c519fdb3e55e07fc40d4adecbbec623c0249380a7eb"


S = "${WORKDIR}/NetworkManager-${PV}"

EXTRA_OECONF = " \
		--with-distro=debian \
		--with-crypto=gnutls \
		--disable-more-warnings \
        --with-dhclient=${base_sbindir}/dhclient \
        --with-iptables=${sbindir}/iptables \
        --enable-ppp \
	--with-modem-manager-1 \
"

do_configure_prepend() {
    cp ${WORKDIR}/gtk-doc.make ${S}/
    echo "EXTRA_DIST = version.xml" > gnome-doc-utils.make
    sed -i -e s:docs:\:g ${S}/Makefile.am
    sed -i -e /^docs/d ${S}/configure.ac
}

# Work around dbus permission problems since we lack a proper at_console
do_install_prepend() {
	sed -i -e s:deny:allow:g ${S}/src/org.freedesktop.NetworkManager.conf
	sed -i -e s:deny:allow:g ${S}/system-settings/src/nm-system-settings.conf || true
	sed -i -e s:deny:allow:g ${S}/callouts/nm-dispatcher.conf
}

do_install_append () {
	install -d ${D}/etc/dbus-1/event.d
	# Test binaries
	install -d ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/nm-tool ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/libnm* ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/nm-online ${D}/usr/bin

	install -d ${D}/etc/NetworkManager/
	install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}/etc/NetworkManager/
	install -m 0644 ${WORKDIR}/org.freedesktop.NetworkManager.conf ${D}/etc/NetworkManager/


	install -d ${D}/etc/NetworkManager/system-connections/
	install -m 0600 ${WORKDIR}/keyfile-T-mobile ${D}/etc/NetworkManager/system-connections/T-mobile
	install -m 0600 ${WORKDIR}/keyfile-Verizon ${D}/etc/NetworkManager/system-connections/Verizon

	# Install an empty VPN folder as nm-connection-editor will happily segfault without it :o.
	# With or without VPN support built in ;).
	install -d ${D}/etc/NetworkManager/VPN
}

PACKAGES =+ "libnmutil libnmglib libnmglib-vpn ${PN}-tests" 

FILES_libnmutil += "${libdir}/libnm-util.so.*"
FILES_libnmglib += "${libdir}/libnm_glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm_glib_vpn.so.*"

FILES_${PN} += " \
		${libexecdir} \
		${libdir}/pppd/*/nm-pppd-plugin.so \
		${libdir}/NetworkManager/*.so \
		${datadir}/polkit-1 \
		${datadir}/dbus-1 \
		${base_libdir}/udev/* \
"
RRECOMMENDS_${PN} += "iptables"
RCONFLICTS_${PN} = "connman"
RDEPENDS_${PN} = "wpa-supplicant dhcp-client \
           ${@base_contains('COMBINED_FEATURES', '3gmodem', 'ppp', '', d)} \
           "

FILES_${PN}-dbg += "${libdir}/NetworkManager/.debug/ \
		    ${libdir}/pppd/*/.debug/ "

FILES_${PN}-staticdev += "${datadir}/NetworkManager/gdb-cmd \
                    ${libdir}/pppd/*/*.a \
                    ${libdir}/pppd/*/*.la \
                    ${libdir}/NetworkManager/*.a \
                    ${libdir}/NetworkManager/*.la"

FILES_${PN}-tests = "${bindir}/nm-tool \
                     ${bindir}/libnm_glib_test \
                     ${bindir}/nminfotest \
                     ${bindir}/nm-online \
                     ${bindir}/nm-supplicant \
                     ${bindir}/nm-testdevices"


pkg_postinst_${PN}() {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    
    /etc/init.d/NetworkManager start
}
#systemctl enable NetworkManager.service

pkg_prerm_${PN}() {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi

    /etc/init.d/NetworkManager stop
}
#systemctl disable NetworkManager.service
