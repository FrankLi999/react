export interface UserProfile {
    id: string;
    email: string;
    firstName?: string;
    lastName?: string;
    name?: string;
    imageUrl?: string;
    accessToken: string;
    expireIn: number;
    roles: string[];
    tokenType: string;
}

export interface Login {
    userName: String;
    password: String;
}

// @ts-ignore
export const login = (login: Login) => {
    // this._authService.login(login).pipe(
    //   filter((userProfile) => !!userProfile),
    //   tap((userProfile) => {
    //     this._uiService.saveUserProfile(userProfile);
    //     this.loginSuccessful(userProfile);
    //   }),
    //   // map((userProfile: UserProfile) => {
    //   //   return loginSucceedAction({payload: userProfile as UserProfile});
    //   // }),
    //   catchError((err) => {
    //     this.loginFailed(err);
    //     return of(err);
    //   })).subscribe();
}

// @ts-ignore
export const loginSuccessful = (userProfile: UserProfile) => {
    
}

export const getUserProfile = () => {
    let userProfile: UserProfile | null= null;
    if (sessionStorage.getItem("bpw_accessToken")) {
        userProfile = {
            id: sessionStorage.getItem("bpw_uid") as string,
            email: sessionStorage.getItem("bpw_email")  as string,
            firstName: sessionStorage.getItem("bpw_firstName") ? sessionStorage.getItem("bpw_firstName")  as string : undefined,
            lastName: sessionStorage.getItem("bpw_lastName") ?  sessionStorage.getItem("bpw_lastName") as string : undefined,
            name: sessionStorage.getItem("bpw_name") ? sessionStorage.getItem("bpw_name") as string : undefined,
            imageUrl: sessionStorage.getItem("bpw_imageUrl") ? sessionStorage.getItem("bpw_imageUrl") as string : undefined,
            accessToken: sessionStorage.getItem("bpw_accessToken") as string,
            roles: (sessionStorage.getItem("bpw_roles") as string).split(","),
            tokenType: sessionStorage.getItem("bpw_tokenType") as string,
            expireIn: parseInt(sessionStorage.getItem("bpw_expireIn") as string),
        };
    }
    return userProfile;
}

export const saveUserProfile = (userProfile) => {
    sessionStorage.setItem("bpw_uid", userProfile.id);
    sessionStorage.setItem("bpw_email", userProfile.email);
    if (userProfile.firstName) {
        sessionStorage.setItem("bpw_firstName", userProfile.firstName);
    }
    if (userProfile.lastName) {
        sessionStorage.setItem("bpw_lastName", userProfile.lastName);
    }
    if (userProfile.name) {
        sessionStorage.setItem("bpw_name", userProfile.name);
    }
    if (userProfile.imageUrl) {
        sessionStorage.setItem("bpw_imageUrl", userProfile.imageUrl);
    }
    sessionStorage.setItem("bpw_accessToken", userProfile.accessToken);
    sessionStorage.setItem("bpw_expireIn", `${userProfile.expireIn}`);
    sessionStorage.setItem(
        "bpw_roles",
        userProfile.roles ? userProfile.roles.join(",") : ""
    );
    sessionStorage.setItem("bpw_tokenType", userProfile.tokenType);
}

export const isLoggedIn = (): boolean => {
    return sessionStorage.removeItem("bpw_accessToken") != null;
}

export const clearUserProfile = () => {
    sessionStorage.removeItem("bpw_uid");
    sessionStorage.removeItem("bpw_email");
    sessionStorage.removeItem("bpw_firstName");
    sessionStorage.removeItem("bpw_lastName");
    sessionStorage.removeItem("bpw_name");
    sessionStorage.removeItem("bpw_imageUrl");
    sessionStorage.removeItem("bpw_accessToken");
    sessionStorage.removeItem("bpw_roles");
    sessionStorage.removeItem("bpw_expireIn");
    sessionStorage.removeItem("bpw_tokenType");
}