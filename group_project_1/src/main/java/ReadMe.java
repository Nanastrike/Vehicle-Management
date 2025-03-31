
/*
com.ptfms.models        // 数据模型（实体类）
com.ptfms.presentation  // 界面层，包含 Servlets（类似 MVP 的 View）
com.ptfms.business      // 业务逻辑层（类似 MVP 的 Presenter）
com.ptfms.data          // 数据访问层（DAO）
com.ptfms.utils         // 工具类（数据库、日志等）

*----------------
* 如何使用git进行团队合作：
* 刚开始：
* 先把主干的代码拉到本地（克隆），再checkout一个自己的branch
* 然后就可以在自己的branch开始写代码
* 
* 日常写代码：
* 先把主干代码更到最新，因为其他人可以提交了内容
* 把主干的代码合并到自己的branch
* 然后在自己的branch开始写代码
* 写完后先push自己的分支到git
* 然后再合并到主干的git
* 
* 注意：如果主干和分支有冲突，优先修改分支代码
* 
* 核心：始终保持和主干最新代码一致，自己本地代码改对了再更到主干，不要在主干改代码，主干只是作为最终存档 
 */