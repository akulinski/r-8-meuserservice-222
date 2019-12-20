import { IUser } from 'app/core/user/user.model';

export interface IUserProfile {
  id?: number;
  currentRating?: number;
  user?: IUser;
}

export class UserProfile implements IUserProfile {
  constructor(public id?: number, public currentRating?: number, public user?: IUser) {}
}
