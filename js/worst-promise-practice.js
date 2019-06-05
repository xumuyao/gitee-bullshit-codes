//看到这些代码已经懒得改了...
export default class ActionInfo extends React.Component{
    emitSubmit=()=>{
        let {state,props} = this;
        this.form.getFormData().then(formdata=>{
            this.setState({
                loading:true
            })
            request('...',{
                body:formdata,
            }).then(res=>{
                this.setState({
                    loading:false,
                })
                if(res.success){
                    message.success('操作成功');
                    this.closeModal();
                    props.successCallback && props.successCallback(res.data);
                }
            })
        })
    }
}
