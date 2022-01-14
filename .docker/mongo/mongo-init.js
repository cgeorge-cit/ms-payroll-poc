print('###Creating user for the application###');
db.createUser(
        {
            user: "admin",
            pwd: "admin",
            roles: [
                {
                    role: "readWrite",
                    db: "payroll"
                }
            ]
        }
);