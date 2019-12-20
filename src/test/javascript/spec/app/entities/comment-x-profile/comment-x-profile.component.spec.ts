import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { CommentXProfileComponent } from 'app/entities/comment-x-profile/comment-x-profile.component';
import { CommentXProfileService } from 'app/entities/comment-x-profile/comment-x-profile.service';
import { CommentXProfile } from 'app/shared/model/comment-x-profile.model';

describe('Component Tests', () => {
  describe('CommentXProfile Management Component', () => {
    let comp: CommentXProfileComponent;
    let fixture: ComponentFixture<CommentXProfileComponent>;
    let service: CommentXProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [CommentXProfileComponent],
        providers: []
      })
        .overrideTemplate(CommentXProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentXProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommentXProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CommentXProfile(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.commentXProfiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
