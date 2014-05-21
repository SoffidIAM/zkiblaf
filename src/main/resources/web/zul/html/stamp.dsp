<%--
stamp.dsp


Copyright (C) 2008 Govern de les illes Balears

--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<c:set var="sessionId" value="${pageContext.session.id}"/>



<div id="${self.uuid}" style="position:relative;top:0px;left:0px;<c:if test="${self.bgHeight!=null}">height:${self.bgHeight}px;</c:if><c:if test="${self.bgWidth!=null}">width:${self.bgWidth}px;</c:if>overflow:hidden;border:1px solid black" z.type="zul.stamp.Stamp">
	<img id="${self.uuid}!im_moving" 
			src="${self.stampSrc}" 
			height="${self.stampHeight}" 
			width="${self.stampWidth}" 
			style="z-index:2;position:relative;top:${self.stampTop}px;left:${self.stampLeft}px;"
	/>
	<br/>
	<img id="${self.uuid}!im_panel" 
		src="${self.bgSrc}" 
		style="z-index:1;position:relative;top:-${self.stampHeight}px;"
	/>
</div>
<div id="debug_panel" style="position:relative;top:0px;left:0px;background-color:green;height:30px;width:700px;overflow:hidden;display:none"></div>
