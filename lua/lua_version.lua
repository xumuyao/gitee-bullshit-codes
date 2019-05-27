local f = function() return function() end end
local t = {
  nil,
  [false]  = 'Lua 5.1',
  [true]   = 'Lua 5.2',
  [1/'-0'] = 'Lua 5.3',
  [1]      = 'LuaJIT'
}

local version = t[1] or t[1/0] or t[f()==f()]
print(version)

--[[
 网上大神根据lua 各个版本的特性写的
 通过判断 每个版本的table实现和function 实现的不同
 来获得运行的lua 版本
]]