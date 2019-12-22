export interface IFollowerXFollowed {
  id?: number;
  followerId?: number;
  followedId?: number;
}

export class FollowerXFollowed implements IFollowerXFollowed {
  constructor(public id?: number, public followerId?: number, public followedId?: number) {}
}
