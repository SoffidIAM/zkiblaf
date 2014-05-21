<%--
applet.dsp


Copyright (C) 2008 Govern de les illes Balears

--%><%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<c:set var="sessionId" value="${pageContext.session.id}"/>
<div id="${self.uuid}" style="height:${self.height};width:${serlf.width};" z.type="zul.sign.Sign">
<c:if test="${self.visible}">
	<object id="${self.uuid}!obj"  width="${self.width}" height="${self.height}"
	    classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
	    align="baseline" 
	    codebase="https://java.sun.com/update/1.5.0/jinstall-1_5_0_12-windows-i586.cab" >
	    <param name="code" value="es.caib.zkiblaf.applet.SignApplet" />
	    <param name="zkuuid" value ="${self.uuid}" />
	    <param name="archive" value="${c:encodeURL("~./signaturacaib.core-api-unsigned.jar")},${c:encodeURL("~./zkiblafsig/zkiblafsig.jar")},${c:encodeURL("~./zkiblafsig/bcmail-unsigned.jar")},${c:encodeURL("~./zkiblafsig/bcprov-unsigned.jar")},${c:encodeURL("~./zkiblafsig/itext.jar")}" />
	    <param name="mayscript" value="true" />
	    <param name="scriptable" value="true" />
	    <param name="signaturaPropertiesServletURL" value="${self.signaturaPropertiesServletURL}"/>
	    <param name="jump" value="javascript:firmado();" />
	        <embed 
			   id="${self.uuid}!emb" 
 			   width="${self.width}" 
 			   height="${self.height}"
 			   align="baseline" 
	           code="es.caib.zkiblaf.applet.SignApplet" 
	           archive='${c:encodeURL("~./signaturacaib.core-api-unsigned.jar")},${c:encodeURL("~./zkiblafsig/zkiblafsig.jar")},${c:encodeURL("~./zkiblafsig/bcmail-unsigned.jar")},${c:encodeURL("~./zkiblafsig/bcprov-unsigned.jar")},${c:encodeURL("~./zkiblafsig/itext.jar")}'
			    signaturaPropertiesServletURL="${self.signaturaPropertiesServletURL}" 
			    zkuuid="${self.uuid}"
			    jump="javascript:firmado();"
	            type="application/x-java-applet"
	            pluginspage="https://java.sun.com/j2se/1.5.0/download.html"
	            mayscript="true"
	            scriptable="true"
	            cache_option="No">
	            <noembed>
	                No te suport per applets Java 2 SDK, Standard Edition v 1.5 ! !
	            </noembed>
	        </embed>
	</object>
    </c:if>
</div>
