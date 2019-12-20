import { Moment } from 'moment';
import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';

export interface IComment {
  id?: number;
  comment?: string;
  timeStamp?: Moment;
  commentXProfile?: ICommentXProfile;
}

export class Comment implements IComment {
  constructor(public id?: number, public comment?: string, public timeStamp?: Moment, public commentXProfile?: ICommentXProfile) {}
}
