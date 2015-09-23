Ext.apply(Ext, {
	flying : {
		init : function(){			
			Ext.Ajax.on('requestexception',function(conn,response,opti){
				var obj = Ext.util.JSON.decode(response.responseText);
				if(response.status == 600){//自定义相应编码
					//var reg = new RegExp("^[0-9a-zA-Z]+[\.](html|htm)$");
					//if(reg.test(obj.msg)){
						window.location = obj.msg; //如重定向到登陆页面 
					//}
				}
			});
			
			//修原生态的TextArea
			Ext.form.TextArea.prototype.flyingResize = function(){
		    	this.ResizableObj = new Ext.Resizable(this.el, {
				    wrap:true,
				    pinned:false,
				    dynamic: true
				});
		    }
		}
	}	
});

Ext.flying.init();