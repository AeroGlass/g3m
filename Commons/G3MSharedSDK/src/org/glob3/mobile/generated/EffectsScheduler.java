package org.glob3.mobile.generated; 
public class EffectsScheduler
{

  private static class EffectRun
  {
	public Effect _effect;
	public EffectTarget _target;

	public boolean _started;

	public EffectRun(Effect effect, EffectTarget target)
	{
		_effect = effect;
		_target = target;
		_started = false;
	}

	public void dispose()
	{
	  if (_effect != null)
		  _effect.dispose();
	}
  }


  private java.util.ArrayList<EffectRun> _effectsRuns = new java.util.ArrayList<EffectRun>();
  private ITimer _timer;
  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE


  private void processFinishedEffects(RenderContext rc, TimeInterval now)
  {
	java.util.ArrayList<Integer> indicesToRemove = new java.util.ArrayList<Integer>();
	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started == true)
	  {
		if (effectRun._effect.isDone(rc, now))
		{
		  effectRun._effect.stop(rc, now);
  
		  indicesToRemove.add(i);
		}
	  }
	}
  
	// backward iteration, to remove from bottom to top
	for (int i = indicesToRemove.size() - 1; i >= 0; i--)
	{
	  final int indexToRemove = indicesToRemove.get(i);
	  if (_effectsRuns.get(indexToRemove) != null)
		  _effectsRuns.get(indexToRemove).dispose();
  
  	_effectsRuns.remove(indexToRemove);
	}
  }

  public EffectsScheduler()
  {
	  _effectsRuns = new java.util.ArrayList<EffectRun>();

  }

  public final void doOneCyle(RenderContext rc)
  {
	final TimeInterval now = _timer.now();
  
  
	processFinishedEffects(rc, now);
  
  
	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started == false)
	  {
		effectRun._effect.start(rc, now);
		effectRun._started = true;
	  }
  
	  effectRun._effect.doStep(rc, now);
	}
  }


  //class SampleEffect : public EffectWithDuration {
  //public:
  //  
  //  SampleEffect(TimeInterval duration) : EffectWithDuration(duration) {
  //  }
  //  
  //  virtual void start(const RenderContext *rc,
  //                     const TimeInterval& now) {
  //    EffectWithDuration::start(rc, now);
  //    _lastPercent = 0;
  //  }
  //  
  //  virtual void doStep(const RenderContext *rc,
  //                      const TimeInterval& now) {
  //    const double percent = pace( percentDone(now) );
  //    rc->getNextCamera()->moveForward((percent-_lastPercent)*1e7);
  //    _lastPercent = percent;
  //  }
  //  
  //  virtual void stop(const RenderContext *rc,
  //                    const TimeInterval& now) {
  //    EffectWithDuration::stop(rc, now);
  //  }
  //  
  //  virtual void cancel(const TimeInterval& now) {
  //    // do nothing, just leave the effect in the intermediate state
  //  }
  //
  //private:
  //  double _lastPercent;
  //};
  
  
  
  
  
  public final void initialize(Context context)
  {
	_factory = context.getFactory();
	_timer = _factory.createTimer();
  }

  public void dispose()
  {
	_factory.deleteTimer(_timer);

	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
	  if (effectRun != null)
		  effectRun.dispose();
	}
  }

  public final void startEffect(Effect effect, EffectTarget target)
  {
	_effectsRuns.add(new EffectRun(effect, target));
  }

  public final void cancellAllEffectsFor(EffectTarget target)
  {
	java.util.ArrayList<Integer> indicesToRemove = new java.util.ArrayList<Integer>();
	final TimeInterval now = _timer.now();
  
	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started == true)
	  {
		if (effectRun._target == target)
		{
		  effectRun._effect.cancel(now);
  
		  indicesToRemove.add(i);
		}
	  }
	}
  
	// backward iteration, to remove from bottom to top
	for (int i = indicesToRemove.size() - 1; i >= 0; i--)
	{
	  final int indexToRemove = indicesToRemove.get(i);
	  if (_effectsRuns.get(indexToRemove) != null)
		  _effectsRuns.get(indexToRemove).dispose();
  
  	_effectsRuns.remove(indexToRemove);
	}
  
  }

  public final void onResume(Context context)
  {

  }

  public final void onPause(Context context)
  {

  }

  public final void onDestroy(Context context)
  {

  }


}