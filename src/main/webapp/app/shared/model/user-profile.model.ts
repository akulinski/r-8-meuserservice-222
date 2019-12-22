export interface IUserProfile {
  id?: number;
  currentRating?: number;
  userId?: number;
}

export class UserProfile implements IUserProfile {
  constructor(public id?: number, public currentRating?: number, public userId?: number) {}
}
