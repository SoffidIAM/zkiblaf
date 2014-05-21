var ie=true;
function detectBrowser(){
	if(document.all) ie=true;
	else ie=false;
}

detectBrowser();


function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}



var debuggers= new Array();

function debug(module,message,increment){
	var dbgList=readCookie("ibkey-debug");
	if(dbgList!=null){
		//read enabled debugers from cookie
		
		for(d=0;d<dbgList.split(",").length;d++){
			debuggers[dbgList.split(",")[d]]=true
		}
	}		
	if(debuggers[module]!=null){
		document.getElementById("debug_panel").style.display="block"
		if(increment){
			//alert(document.getElementById("debug_panel").innerHTML)
			 document.getElementById("debug_panel").innerHTML= document.getElementById("debug_panel").innerHTML+" "+message;
			 //alert(document.getElementById("debug_panel").innerHTML+" "+message);
		}else{
			document.getElementById("debug_panel").innerHTML=message;
			//alert(message);
		}
	}
}



//Devuelve el evento segun el browser
CrossBrowserEvent= function(evt){

	 this.internal_getCrossBrowserEventPositionX=function(e){
		if(ie) return e.x;
		else return e.clientX;
	}

	 this.internal_getCrossBrowserEventPositionY= function(e){
		if(ie) return e.y;
		else return e.clientY;
	}

	this.internal_getCrossBrowserEventTarget=function(e){
		if(ie) return e.srcElement
		else return e.target
	}

	this.getX=function () {return this.x};
	this.getY=function () {return this.y};
	


	
	this.x=this.internal_getCrossBrowserEventPositionX(evt);
	this.y=this.internal_getCrossBrowserEventPositionY(evt);
	this.target=this.internal_getCrossBrowserEventTarget(evt);

	
	return this;
}



var PositionController=function(zkid,panel,panel_image,stamp_image){
	this.zkid=zkid;

	this.x="";
	this.y="";
	this.panel=panel;
	this.panel_image=panel_image;
	this.stamp_image=stamp_image;
	this.initialX=this.stamp_image.offsetLeft-((ie)?this.stamp_image.style.posLeft:parseInt(this.stamp_image.style.left));
	this.initialY=this.stamp_image.offsetTop-((ie)?this.stamp_image.style.posTop:parseInt(this.stamp_image.style.top));
	this.lastX=this.initialX;
	this.lastY=this.initialY;
	// captura del movimiento del objeto	 
	this.moving=false;
	this.updated=false;

	this.getCrossBrowserEvent = function(ev){
		

		if (! ev) //MSIE
			return new CrossBrowserEvent(window.event);
		else	//FF
			return new CrossBrowserEvent(ev);		
	}
	
	this.resetPosition = function(){
		this.lastX="";
		this.lastY="";
	}
	
	this.updatePosition = function(ev,forceUpdate){
		var e=this.getCrossBrowserEvent(ev);
		if(this.moving || forceUpdate){
			if(ie){
				if(e.target.id==this.stamp_image.id){
					this.x=e.getX()+e.target.offsetLeft;
					this.y=e.getY()+e.target.offsetTop;
				}else{
					this.x=e.getX();
					this.y=e.getY();
				}
			}else{
				this.x=e.getX();
				this.y=e.getY();
			}			
			debug("move","e.x="+e.getX(),false);
			debug("move"," e.y="+e.getY(),true);
			debug("move"," x="+this.x,true);
			debug("move"," y="+this.y,true);
			debug("move"," offX="+this.stamp_image.offsetLeft,true);
			debug("move"," offY="+this.stamp_image.offsetTop,true);	
		}
	}

	this.updateServerPosition=function(){
		if(this.moving){
			//alert([this.stamp_image.offsetTop,(parseInt(this.stamp_image.offsetLeft)),this.initialX])
			//alert(eval("{uuid:\""+this.zkid+"\", cmd: \"update\", data: [\""+this.y+"\",\""+this.x"\"]}"))
			eval("zkau.send({uuid:\""+this.zkid+"\", cmd: \"update\", data: [\""+this.stamp_image.offsetTop+"\",\""+(parseInt(this.stamp_image.offsetLeft))+"\"]})");			
		}
	}


	this.onmousemove_image=function (ev){
		//alert(this.id)
		//aqui this es la imagen
		this.controller.updatePosition(ev,false);
		this.controller.animation_event_timeout()
	}

	this.onmouseclick_image=function (ev){
		
		//aqui this es la imagen
		this.controller.updateServerPosition();
		this.controller.moving=!this.controller.moving;

		this.controller.resetPosition();
		this.controller.updatePosition(ev,true);
		
	}


	this.onmouseclick_panel=function (ev){

	/**
	 * Si el ratón se ha salido de la imagen que se mueve
	 */
		
		//aqui this es la imagen
		//aqui this es la imagen
		if(this.controller.moving){
			this.controller.moving=false;
			this.controller.resetPosition();
			this.controller.updatePosition(ev,true);
		}else{
			this.controller.moving=true;
			this.controller.resetPosition();
			if(ie){
				this.controller.lastX=parseInt(this.controller.stamp_image.offsetLeft);
				this.controller.lastY=parseInt(this.controller.stamp_image.offsetTop);
			}else{
				this.controller.lastX=parseInt(this.controller.panel.offsetLeft)+parseInt(this.controller.stamp_image.offsetLeft);
				this.controller.lastY=parseInt(this.controller.panel.offsetTop)+parseInt(this.controller.stamp_image.offsetTop);
			}

			this.controller.updatePosition(ev,true);
			this.controller.animation_event_timeout()
			this.controller.updateServerPosition();
			this.controller.moving=false;
			this.controller.updated=false;
			
		}
	}

	this.animation_event_timeout=function (){
		var distx=0;
		var disty=0;
		//this.moving=false
		
		if(this.moving){
			this.moving=false;

			if(""+this.lastX!=""){
				debug("event",this.moving,false);
				debug("event",new Date().getTime());
				debug("event","x="+this.x,true);
				debug("event"," y="+this.y,true);
				debug("event","lastX="+this.lastX,true);
				debug("event"," lastY="+this.lastY,true);
				if(!this.updated){
					distx=this.x-this.lastX;
					disty=this.y-this.lastY;
			
					this.lastX=this.x;
					this.lastY=this.y;
				}

				debug("event","distX="+distx,true);
				debug("event"," distY="+disty,true);
				debug("event"," oldY="+(this.stamp_image.offsetTop ),true);
				debug("event"," olfX="+(this.stamp_image.offsetLeft),true);
				debug("event"," updated="+this.updated,true);
				//alert(1);
				if(ie){
					if(!this.updated){	
						if(distx!=0 || disty!=0){
							var newY=parseInt(this.stamp_image.offsetTop)+parseInt(disty);
							var newX=parseInt(this.stamp_image.offsetLeft)-this.initialX+parseInt(distx);

							
							debug("event"," newY="+newY,true);
							debug("event"," newX="+newX,true);
							this.stamp_image.style.posTop=newY;
							this.stamp_image.style.posLeft=newX;
							this.updated=true;
							
						}
					}else{
						this.updated=false;		
					}
				}else{

					if(distx!=0 || disty!=0){
						var newY=parseInt(this.stamp_image.offsetTop)+parseInt(disty)+"px";
						var newX=parseInt(this.stamp_image.offsetLeft)-this.initialX+parseInt(distx)+"px";
						
						debug("event"," newY="+newY,true);
						debug("event"," newX="+newX,true);
						//alert(1)
						this.stamp_image.style.top=newY;
						this.stamp_image.style.left=newX;
					}
					
				}
			}else{
				distx=0;
				disty=0;
				this.lastX=this.x;
				this.lastY=this.y;			
			}
		this.moving=true;
		}	
		
	}

	/**
	alert([this.x,this.y,this.lastX,this.lastY])
	alert(this.getCrossBrowserEvent)
	alert(this.resetPosition)
	alert(this.updatePosition)
	alert(this.onmousemove_image)
	alert(this.onmouseclick_image)
	alert(this.onmouseclick_panel)
	alert(this.animation_event_timeout)
	**/


	this.init=function(){
		this.panel_image.controller=this;
		this.stamp_image.controller=this;
	
		if(ie){
			this.panel_image.onmousemove=this.onmousemove_image
			this.stamp_image.onmousemove=this.onmousemove_image
			this.stamp_image.onmousedown=this.onmouseclick_image
			this.panel_image.onclick=this.onmouseclick_panel
		}else{
			this.panel_image.addEventListener("mousemove",this.onmousemove_image,true);
			this.stamp_image.addEventListener("mousemove",this.onmousemove_image,true);
			this.stamp_image.addEventListener("mousedown",this.onmouseclick_image,true);
			this.panel_image.addEventListener("click",this.onmouseclick_panel,true);
		}
		
		//inicialización de la posición
		this.moving=true;
		this.updateServerPosition();
		this.moving=false;
	}

	//configuración
	this.init();
	
	return this;
}


///////////////////INICI DE JS PER A ZK /////////////////////////////


zkStamp={};

zkStamp.init=function(_1){
	new PositionController(_1.id,document.getElementById(_1.id),document.getElementById(_1.id+"!im_panel"),document.getElementById(_1.id+"!im_moving"));
};

zkStamp.cleanup=function(_2){
};


