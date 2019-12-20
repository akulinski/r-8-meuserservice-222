import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IComment } from 'app/shared/model/comment.model';

export interface ICommentXProfile {
  id?: number;
  receiver?: IUserProfile;
  poster?: IUserProfile;
  comment?: IComment;
}

export class CommentXProfile implements ICommentXProfile {
  constructor(public id?: number, public receiver?: IUserProfile, public poster?: IUserProfile, public comment?: IComment) {}
}
