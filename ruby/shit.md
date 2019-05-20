重复delete
```
unless can?(current_user, :modify_issue, @project)
  params[:issue].delete(:assignee_id)
  params[:issue].delete(:milestone_id)
  params[:issue].delete(:label_list)
  params[:issue].delete(:collaborators_ids)
end
```
修改如下：
```
params[:issue].except!(:assignee_id, :milestone_id, :label_list, :collaborators_ids)
```
看起来清爽了很多
