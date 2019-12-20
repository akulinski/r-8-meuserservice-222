import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { CommentXProfileDetailComponent } from 'app/entities/comment-x-profile/comment-x-profile-detail.component';
import { CommentXProfile } from 'app/shared/model/comment-x-profile.model';

describe('Component Tests', () => {
  describe('CommentXProfile Management Detail Component', () => {
    let comp: CommentXProfileDetailComponent;
    let fixture: ComponentFixture<CommentXProfileDetailComponent>;
    const route = ({ data: of({ commentXProfile: new CommentXProfile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [CommentXProfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CommentXProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommentXProfileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.commentXProfile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
