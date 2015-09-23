Ext.namespace("Ext.flying");

Ext.flying.TreeLoader = function(config){
    this.baseParams = {};
    
    Ext.apply(this, config);
    
    Ext.flying.TreeLoader.superclass.constructor.call(this);
};

Ext.extend(Ext.flying.TreeLoader, Ext.tree.TreeLoader, {
	processResponse : function(response, node, callback){
        var json = Ext.util.JSON.decode(response.responseText);
        try {
            var o = json["data"];
            
            node.beginUpdate();
            for(var i = 0, len = o.length; i < len; i++){
                var n = this.createNode(o[i]);
                if(n){
                    node.appendChild(n);
                }
            }
            node.endUpdate();
            if(typeof callback == "function"){
                callback(this, node);
            }
        }catch(e){
            this.handleFailure(response);
        }
    }
});