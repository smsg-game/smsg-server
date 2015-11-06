
Ext.onReady(function(){

	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'username'},
		{name: 'active_code'},
		{name: 'success'},
		{name: 'created_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/email_send_log_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
       
    store.on('beforeload', function() {  
       var username = Ext.get('query_key').getValue();
       this.baseParams = {  
         'username': username,
       };  
    });  
     							         
    var data_grid_panel = new Ext.grid.GridPanel({
        store: store,
        width: 600,
        height: 645,
        region:'center',
        loadMask: true,
        columns: [
           new Ext.grid.RowNumberer()
        ,{
            id: 'id',
            header: '编号',
            dataIndex: 'id',
            width: 55,
        },{
            id: 'username',
            header: '用户名',
            dataIndex: 'username',
            width: 250,
        },{
            id: 'active_code',
            header: '激活码',
            dataIndex: 'active_code',
            width: 700,
        },{
            id: 'success',
            header: '结果',
            dataIndex: 'success',
            width: 50,
            renderer: function(value){
                if(value){
                    return "<font color=\"green\">成功</font>";
                }else{
                    return "<font color=\"red\">失败</font>";
                }
            }
        },{
            id: 'created_time',
            header: '发送时间',
            dataIndex: 'created_time',
            width: 150,
        },],
        tbar: [{ 
        	xtype: 'textfield',
            name: 'query_key',
            id:'query_key',
        },{
        	ref: '../queryBtn',
            iconCls: 'task-query',
            text: '查询',
            handler: function(){
            	store.load();
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
    var downurl_panel = new Ext.Panel({
        title: '激活邮件发送记录',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
});