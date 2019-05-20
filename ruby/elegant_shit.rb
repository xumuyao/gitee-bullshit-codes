# 需求：
# 输入: candidates = [2,3,6,7], target = 7,
# 所求解集为:
#     [
#         [7],
#         [2,2,3]
#     ]

# 一开始的实现，看起来贼乱也不好理解

def combination_sum(candidates, target)
  result=Array.new
  manage_num(candidates, target, result)
  result.each {|re| re.sort!}
  result.sort
end

def manage_num(candidates, target, result, num_array=Array.new, sum=0)
  if target==sum
    result.push num_array
    return
  else
    for i in 0.upto(candidates.size-1)
      if target>=sum+candidates[i]
        tmpNumsNew=num_array.dup
        tmpNumsNew.push candidates[i]
        manage_num(candidates[i..-1], target, result, tmpNumsNew, sum+candidates[i])
      end
    end
  end
end


#ruby要优雅～～～～，善于使用ruby方法

def combination_sum(candidates, target)
  return [[]] if target == 0
  return [] if target < candidates.min
  candidates.flat_map{|c| combination_sum(candidates,target - c).map {|f| [c,*f].sort } }.uniq #recur
end
