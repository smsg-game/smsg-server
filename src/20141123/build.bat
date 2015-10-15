set DESC=E:\smsg-build

mvn clean install -U -Dmaven.test.skip=true -pl game-server,game-utils,partner-sdk,game-model,game-dao,game-service
