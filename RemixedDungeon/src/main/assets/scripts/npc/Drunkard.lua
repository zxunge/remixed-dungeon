---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by mike.
--- DateTime: 1/17/21 11:19 PM
---

local RPD = require "scripts/lib/commonClasses"

local mob = require"scripts/lib/mob"


return mob.init({
    interact = function(self, chr)
        self:say("Wanna drink?")
    end
})
