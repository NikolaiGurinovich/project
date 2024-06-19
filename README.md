This is the readme file of my application 
It represents kind of social media for people who involved in sport, here users can uplouad their training session, and other users can see and like it. Also users can make and join groups and subscribe other users

This app is developed with REST architectural style and gives data in JSON format to any request

There are three roles of users: USER, GROUP_ADMIN, ADMIN
USER is the basic role which allows customer use next abilities of app:
1. Registration POST("/security/registration") user enter info about himself and if validation is successful user becom a member of app "USERS"
2. Get info about himself GET("/user/info")
3. Update info about user PUT("/user/update") user add necessary info in body of request and this will be updated
4. Subscribe on other user POST("/user/subscribe/{id}") user enter the id of target user and if this user exist - subscribe him
5. Unsubcribe from user DELETE("/user/subscribe/{id}") user enter the id of target user and unsubscribe from him
6. Add workout POST("/workout/add") user add info about his workout in body and upload it in db
7. Get info about any workout GET("/workout/{id}") user enter the id of target workout and gets info about it
8. Like other workout POST("/workout/like/{id}") user enter the id of target workout and this workout gets like
9. Create his own group POST("/group/create") user enter group name, and if it valid app add this group to db, and user become a GROUP_ADMIN
10. Join other group POST("/group/join/{id}) user enter the id of target group and become the member
11. Leave group DELETE("/group/leave/{id") user enter the id of target group and is no longer a member of it
GROUP_ADMIN is the second role, to become it USER must create his one group, it has all of possibilities of USER plus some special ones:
1. Update his group name PUT("/group/update/{id}") group admin enters the id of target group and new groupName after that info in DB is updating
2. Delete member from his own group DELETE("/group/user/{userId}/{groupId}") group admin enters the id of target group and user ane this user is no longer a mrmber of this group
3. Delete his own group ("/group/delte/{id}") group admin enters the id of target group and this group is deleting from DB
ADMIN is the third role that has all of possibilities of GROUP_ADMIN plus some special ones:
1. Delete any user from any group DELETE("/group/user/{userId}/group/{groupId}") admin enters the id of target group and user ane this user is no longer a mrmber of this group
2. Make other user an ADMIN PUT("/user/admin/{id}") admin enters the id of target user and it become an admin too
3. Also aadmin have rights to CRUD operations in each comtroller, so he can change any info in all tables in DB
   
This APP use postgress DP which contains next tables:
1. users - info about users
2. security - security info about each user
3. workout - info about all workouts
4. groups - info about all groups
5. likes - link table to save likes to workout
6. followers - link table to save follows
7. l_user_group - link table to save users in groups

This app is secure by JWT security:
the expiration time of TOKEN is 10 minutes
To get a token you should send GET("/security/token") 
