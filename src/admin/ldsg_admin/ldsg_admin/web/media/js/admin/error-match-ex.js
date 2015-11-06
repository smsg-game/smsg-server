
Ext.onReady(function(){
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'match_id'},
		{name: 'type'},
		{name: 'remark'},
		{name: 'client'},
		{name: 'created_time'},
	]);
 	
 	var expander = new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
            '<p><b>Company:</b> {remark}</p>'
        )
    });
 	
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/error_match_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
        							         
    var data_grid_panel = new Ext.grid.GridPanel({
        store: store,
        width: 600,
        height: 645,
        plugins: expander,
        region:'center',
        loadMask: true,
        columns: [
           expander,
           new Ext.grid.RowNumberer()
        ,{
            id: 'id',
            header: '编号',
            dataIndex: 'id',
            width: 55,
        },{
            id: 'match_id',
            header: '比赛id',
            dataIndex: 'match_id',
            width: 80,
        },{
            id: 'type',
            header: '比赛类型',
            dataIndex: 'type',
            width: 80,
        },{
            id: 'client',
            header: '客户端',
            dataIndex: 'client',
            width: 80,
        },{
            id: 'remark',
            header: '备注',
            dataIndex: 'remark',
            width: 580,
            renderer: function(value){
              return "<span title=\"" + value + "\">" + value + "<span>";
            }
        },{
            id: 'created_time',
            header: '创建时间',
            dataIndex: 'created_time',
            width: 180,
        },{
            id: 'restart',
            header: '操作',
            dataIndex: 'match_id',
            width: 80,
            renderer: function(value){
              var href = "<a href=\"javascript:void(0);\" onclick=\"javascript:restart('" + value + "');\">重赛</a>";
              return href;
            }
        }],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var data_panel = new Ext.Panel({
        title: '异常比赛查询',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
    restart = function(id){
    	  Ext.Ajax.request({ 
			url : '/admin/error_match_restart/',
			sync : true,
			params : { 
			  match_id: id,
			},
		    success : function(response) { 
		        var data = response.responseText;
		        if(data=='error'){
		        	Ext.Msg.show({ 
				      title : '请求出错', 
				      msg : '服务器异常!', 
				      icon : Ext.Msg.ERROR, 
				      buttons : Ext.Msg.OK 
				    })
		        }else{
		            Ext.Msg.show({ 
				      title : '请求成功', 
				      msg : '重赛任务已经创建!', 
				      icon : Ext.Msg.SUCCESS, 
				      buttons : Ext.Msg.OK 
				    })
		            store.load();
		        }
			    
			}, 
		    failure : function(response){ 
			    Ext.Msg.show({ 
			      title : '请求出错', 
			      msg : '服务器异常!', 
			      icon : Ext.Msg.ERROR, 
			      buttons : Ext.Msg.OK 
			    })
			}
		  }) 
    }
    
    
});