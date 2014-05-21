zkSign={};
zkSign.init=function(_1){
};
zkSign.cleanup=function(_2){
};
zkSign.getCerts=function (cmp,source) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").loadCerts(source);
	else
		$e(cmp.id+"!obj").loadCerts(source);
};
zkSign.sign=function (cmp,cert,source) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").sign(cert,source);
	else
		$e(cmp.id+"!obj").sign(cert, source);
};
zkSign.signPDF=function (cmp,cert,source,target) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").signPDF(cert,source,target);
	else
		$e(cmp.id+"!obj").signPDF(cert, source,target);
};
zkSign.setSignPDF_url=function (cmp,url) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDF_url(url);
	else
		$e(cmp.id+"!obj").setSignPDF_url(url);
};
zkSign.setSignPDF_position=function (cmp,pos) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDF_position(pos);
	else
		$e(cmp.id+"!obj").setSignPDF_position(pos);
};
zkSign.setSignPDFExtended_top=function (cmp,top) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDFExtended_top(top);
	else
		$e(cmp.id+"!obj").setSignPDFExtended_top(top);
};
zkSign.setSignPDFExtended_left=function (cmp,left) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDFExtended_left(left);
	else
		$e(cmp.id+"!obj").setSignPDFExtended_left(left);
};
zkSign.setSignPDFExtended_height=function (cmp,height) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDFExtended_height(height);
	else
		$e(cmp.id+"!obj").setSignPDFExtended_height(height);
};
zkSign.setSignPDFExtended_width=function (cmp,width) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDFExtended_width(width);
	else
		$e(cmp.id+"!obj").setSignPDFExtended_width(width);
};
zkSign.setSignPDFExtended_rotation=function (cmp,rotation) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setSignPDFExtended_rotation(rotation);
	else
		$e(cmp.id+"!obj").setSignPDFExtended_rotation(rotation);
};
zkSign.signPDFExtended=function (cmp,cert,source,target) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").signPDFExtended(cert,source,target);
	else
		$e(cmp.id+"!obj").signPDFExtended(cert, source,target);
};
zkSign.certifyPDF=function (cmp,cert,source,target) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").certifyPDF(cert,source,target);
	else
		$e(cmp.id+"!obj").certifyPDF(cert, source,target);
};
zkSign.setCertifyPDF_x=function (cmp,x) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setCertifyPDF_x(x);
	else
		$e(cmp.id+"!obj").setCertifyPDF_x(x);
};
zkSign.setCertifyPDF_y=function (cmp,y) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setCertifyPDF_y(y);
	else
		$e(cmp.id+"!obj").setCertifyPDF_y(y);
};
zkSign.setCertifyPDF_degrees=function (cmp,deg) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setCertifyPDF_degrees(deg);
	else
		$e(cmp.id+"!obj").setCertifyPDF_degrees(deg);
};
zkSign.setCertifyPDF_url=function (cmp,url) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setCertifyPDF_url(url);
	else
		$e(cmp.id+"!obj").setCertifyPDF_url(url);
};
zkSign.setCertifyPDF_location=function (cmp,location) {
    if ($e(cmp.id+"!emb"))
		$e(cmp.id+"!emb").setCertifyPDF_location(location);
	else
		$e(cmp.id+"!obj").setCertifyPDF_location(location);
};
;

