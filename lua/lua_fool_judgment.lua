function _logout(self)
    local sock = self.sock
    if not sock then
        return nil, "not initialized"
    end

    self.state = nil
    if self.state == STATE_CONNECTED then
        -- Graceful shutdown
        local headers = {}
        headers["receipt"] = "disconnect"
        sock:send(_build_frame(self, "DISCONNECT", headers, nil))
        sock:receive("*a")
    end
    return sock:close()
end

--[[
  这是一段有史以来我见过最'Graceful'的代码, 上面的注释是作者自己加入进去的.
  出处来源于这里: https://github.com/Loc-Tran/lua-resty-rabbitmqstomp/blob/v0.1/lib/resty/rabbitmqstomp.lua
]]
