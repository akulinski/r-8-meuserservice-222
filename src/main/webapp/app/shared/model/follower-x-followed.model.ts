import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IFollowerXFollowed {
  id?: number;
  follower?: IUserProfile;
  followed?: IUserProfile;
}

export class FollowerXFollowed implements IFollowerXFollowed {
  constructor(public id?: number, public follower?: IUserProfile, public followed?: IUserProfile) {}
}
