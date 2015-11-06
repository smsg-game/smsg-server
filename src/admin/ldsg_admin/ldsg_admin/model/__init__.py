
from ldsg_admin.common.bottle.persistable import Persistable


class PartnerService(Persistable):
    pass

class Partner(Persistable):
    pass

class Service(Persistable):
    pass

class User(Persistable):
    pass

class Server(Persistable):
    pass

class StatServerData(Persistable):
    temp = 0.0000
    pass
class StatServerDataCountry(Persistable):
    temp = 0.0000
    pass

class StatUserLevel(Persistable):
    pass

class StatLiucunData(Persistable):
    pass
class StatLiucunDataDate(Persistable):
    pass

class SystemToolExchange(Persistable):
    pass

class SystemToolExchangeDetail(Persistable):
    pass

class SystemTool(Persistable):
    pass

class SystemPayReward(Persistable):
    pass

class SystemPayRewardDetail(Persistable):
    pass

class Page(Persistable):
    user_page_id = 0
    value = 0
    pass

class UserPage(Persistable):
    pass

class StatUserLevelRank(Persistable):
    pass

class Client(Persistable):
    pass

class ClientRunLog(Persistable):
    pass

class SystemMail(Persistable):
    pass

class ToolUseLog(Persistable):
    pass

class Notice(Persistable):
    pass

class ServerPartnerList(Persistable):
    pass

class WhiteIp(Persistable):
    pass

class SystemMallDiscountActivity(Persistable):
    pass

class SystemMallDiscountActivityDetail(Persistable):
    pass

class BattleClient(Persistable):
    pass
class MainServerClient(Persistable):
    pass

if __name__ == "__main__":
    user = User()
    user.username = 'username'
    user.password = 'password'
    user.persist()
    
    user = User.load(username='username')