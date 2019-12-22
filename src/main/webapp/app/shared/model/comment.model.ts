import { Moment } from 'moment';

export interface IComment {
  id?: number;
  comment?: string;
  timeStamp?: Moment;
  commentXProfileId?: number;
}

export class Comment implements IComment {
  constructor(public id?: number, public comment?: string, public timeStamp?: Moment, public commentXProfileId?: number) {}
}
